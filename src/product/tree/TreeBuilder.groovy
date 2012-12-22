package product.tree;


public class TreeBuilder{
	def RuleChainRepository ruleChainRepository
	
	def TreeBuilder(RuleChainRepository ruleChainRepository){
		this.ruleChainRepository = ruleChainRepository
	}
	
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

            path.ruleChain = ruleChainRepository.getRuleChain(xmlPath.chain.@name) 
        }
    }
    
}


