package product.tree

class RuleChainRepository {
	def chainsMap = [:]
	def shell = new GroovyShell()
	
	def RuleChainRepository(String xmlChains, String xmlRules){
		def chainRep = new XmlParser().parseText(xmlChains);
		def ruleRep = new XmlParser().parseText(xmlRules); 
		
		chainRep.each {
			def chain =  convertRuleChain(it, ruleRep)
			chainsMap[chain.name] = chain
		}
	}
	
	def getRuleChain(String name){
		chainsMap[name] 
	}
	
	def private convertRuleChain(xmlChain, ruleRep){
		RuleChain chain = new RuleChain()
		chain.id = xmlChain.@id
		chain.name = xmlChain.@name
		
		xmlChain.rule.each {
			chain.rules += convertRules(it, ruleRep)
		} 
		
		chain.permissionPolicy = {rules, binding->
			rules.every{
				try{
					it.script.binding=binding
					it.script.run()
				}catch(Exception e){
					false
				}
			}
		}
		return chain
	}
	
	def private convertRules(xmlRule, ruleRep){
		def ruleInRep = ruleRep.find{xmlRule.@name == it.@name}
		Rule rule = new Rule()
		rule.name = ruleInRep.@name
		rule.content = ruleInRep.text()
		rule.script  = shell.parse(rule.content)
	   return rule
	}
}

