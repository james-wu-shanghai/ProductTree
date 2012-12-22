package product.tree;


public class TreeBuilder{
    def Tree generateTree(xml){
		
        Tree tree= new Tree()
        def nodes = [] 
		
   		def xmlTree =  new XmlParser().parseText(xml);
		xmlTree.node.each {nodes += convertNode(it)}
		
        assamblePath(nodes, xmlTree.path)
        
        def candidates = nodes.findAll{it.root.is(true)}
        if(candidates.size() != 1){
            throw new IllegalArgumentException("None or more than one root node found. Initialize Tree Builder failed.")
        }
        tree.root = candidates[0];
        return tree
    }
    
    def Node convertNode(groovy.util.Node xmlNode){
        product.tree.Node node = new  product.tree.Node()
        node.id = xmlNode.@id.toInteger()
        node.root = xmlNode.@root
        return node
    }
    
   def assamblePath(nodes, xmlPaths){
        xmlPaths.each {xmlPath->
            Path path = new Path();

			path.type = xmlPath.@type
						
            path.from = nodes.find{it.id + "" == xmlPath.from.@id[0]}
            path.from.tos += path
			
            path.to = nodes.find{it.id+"" == xmlPath.to.@id[0]}
            path.to.froms += path

            path.ruleChain = convertRuleChain(xmlPath.chain)
        }
    }
    
    def convertRuleChain(xmlChain){
        RuleChain chain = new RuleChain()
        chain.id = xmlChain.@id[0]
        chain.name = xmlChain.@name[0]
        
        chain.rules = convertRules(xmlChain.rule)
        
		
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
	
    def convertRules(xmlRules){
		def shell = new GroovyShell()
		def rules = []
		xmlRules.each{
			Rule rule = new Rule()
			rule.content = it.@name[0]
			rule.content = it.text()
			rule.script  = shell.parse(rule.content)
			rules += rule
       }
       return rules
    }
}


