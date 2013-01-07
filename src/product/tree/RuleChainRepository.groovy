package product.tree

class RuleChainRepository {
    def chainsMap = [:]
    def ruleMap=[:]
    def shell = new GroovyShell()

    def RuleChainRepository(String xmlChains, String xmlRules){
        def chainRep = new XmlParser().parseText(xmlChains);
        def ruleRep = new XmlParser().parseText(xmlRules);


        ruleRep.each{
            Rule rule = new Rule()
            rule.name = it.@name
            rule.content = it.text()
            rule.script  = shell.parse(rule.content)
            ruleMap[rule.name]=rule
        }

        chainRep.each {
            def chain =  convertRuleChain(it)
            chainsMap[chain.name] = chain
        }
    }

    def getRuleChain(String name){
        return chainsMap[name]
    }

    def private convertRuleChain(xmlChain){
        RuleChain chain = new RuleChain()
        chain.name = xmlChain.@name

        xmlChain.rule.each {
            Rule rule = ruleMap[it.@name].clone()
            if(it.@pass?.equalsIgnoreCase("false")){
                rule.pass=false
            }
            chain.rules += rule
        }

        switch(chain.type){
            case 'OR':
                chain.permissionPolicy =  {rules, binding->
                    boolean chainResult = rules.any{
                        try{
                            it.script.binding=binding
                            boolean result = it.script.run()
                            return it.pass?result:!result
                        }catch(Exception e){
                            throw new IllegalArgumentException(e);
                        }
                    }
                    return chain.pass?chainResult:!chainResult
                }
                break;
            case 'AND':
            default :
                chain.permissionPolicy = {rules, binding->
                    def chainResult = rules.every{
                        try{
                            it.script.binding=binding
                            boolean result = it.script.run()
                            return it.pass?result:!result
                        }catch(Exception e){
                            throw new IllegalArgumentException(e);
                        }
                    }
                    return chain.pass?chainResult:!chainResult
                }
                break;
        }

        return chain
    }
}