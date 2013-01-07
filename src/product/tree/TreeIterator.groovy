package product.tree

class TreeIterator {
	def visitedPath = []
	def candidateNodes = []
	
	def collectNodes(Node node, String type, binding){
		node.tos.each {path->
    		println path.from.id
    		println path.to.id
			if(!visitedPath.contains(path)){
			    visitedPath += path
				if(path.type == type && isValidFromNode(path.from, binding)){
                    binding.currentNode= path.from
                    binding.targetNode = path.to
					if (path.ruleChain.permissionPolicy(path.ruleChain.rules, binding)){
						candidateNodes += path.to
					}
				}
				collectNodes(path.to, type, binding);
			}
		}
        
        def candidateCodes = candidateNodes.nodeOffer?.collect{
            it.code
        }.unique()
        return candidateCodes
	}
    
    def getAccInfoProductCodes(Binding binding){
        def varKeyset = binding.getVariables().keySet()
        if(varKeyset == [] || !varKeyset.contains("accountInfo")){
            return []
        }
        println binding.accountInfo?.userProducts?.productCode
        return binding.accountInfo?.userProducts?.productCode
    }
    
    def isValidFromNode(node, Binding binding){
        // is a root node or node product code in accountInfo which is an offer
        // if is an instance, keep going, the judgment will be put in rule chain
        if(node.root) {return true}
        switch(node.nodeOffer.type){
            case 'ITEM':
                println getAccInfoProductCodes(binding)?.contains(node?.nodeOffer?.code)
                return getAccInfoProductCodes(binding)?.contains(node?.nodeOffer?.code)
            default: 
                return true
        }
    }
	
	def collectNodes(Tree tree, String type, binding){
        return collectNodes(tree.root, type, binding)
	}
	
	def collectNodes(Tree tree, String type){
		collectNodes(tree, type, new Binding())
	}
}
