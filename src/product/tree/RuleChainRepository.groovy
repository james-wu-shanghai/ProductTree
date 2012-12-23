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
		chainsMap[name]
	}

	def private convertRuleChain(xmlChain){
		RuleChain chain = new RuleChain()
		chain.id = xmlChain.@id
		chain.name = xmlChain.@name

		xmlChain.rule.each {
			chain.rules += ruleMap[it.@name]
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
}