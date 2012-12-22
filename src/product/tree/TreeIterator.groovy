package product.tree

class TreeIterator {
	def visitedPath = []
	def candidateNodes = []
	
	def collectNodes(Node node, String type){
		node.tos.each {path->
			if(!visitedPath.contains(path)){
				if(path.type == type){
					if (path.ruleChain.permissionPolicy()){
						candidateNodes+=path.to
					}
					visitedPath+=path
				}
				collectNodes(path.to, type);
			}
		}
		
		return candidateNodes
	}
	
	def collectNodes(Tree tree, String type){
		return collectNodes(tree.root, type)
	}
}
