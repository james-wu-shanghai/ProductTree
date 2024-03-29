package product.tree;

import groovy.transform.CompileStatic;

public class TreeBuilder{
	def RuleChainRepository ruleChainRepository
        
	def TreeBuilder(RuleChainRepository ruleChainRepository){
		this.ruleChainRepository = ruleChainRepository
	}
	
    def Tree generateTree(xml){
        Tree tree= new Tree()
        def nodes = [] 
		
   		def xmlTree =  new XmlParser().parseText(xml);
        tree.name = xmlTree.@name
		xmlTree.node.each {nodes += convertNode(it)}
		
        assamblePath(nodes, xmlTree.path)
        
        def candidates = nodes.findAll{it.root.is(true)}
        if(candidates.size() != 1){
            throw new IllegalArgumentException("None or more than one root node found. Initialize Tree Builder failed.")
        }
        tree.root = candidates[0];
        return tree
    }
    
    def private convertNode(groovy.util.Node xmlNode){
        product.tree.Node node = new product.tree.Node()
        node.id = xmlNode.@id.toInteger()
        if(xmlNode.offer.size() > 0){
            node.nodeOffer = convertOffer(xmlNode.offer[0])
        }
        node.root = xmlNode.@root
        return node
    }
    
    def private convertOffer(xmlOffer){
         NodeOffer offer = new NodeOffer()
         offer.code=xmlOffer.@code
         offer.type=xmlOffer.@type
         return offer   
    }
    
   def private assamblePath(nodes, xmlPaths){
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