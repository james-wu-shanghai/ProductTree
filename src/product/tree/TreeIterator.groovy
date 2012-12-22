package product.tree

class TreeIterator {
	def visitedPath = []
	def candidateNodes = []
	
	def collectNodes(Node node, String type, binding){
		node.tos.each {path->
			if(!visitedPath.contains(path)){
				if(path.type == type){
					if (path.ruleChain.permissionPolicy(path.ruleChain.rules, binding)){
						candidateNodes += path.to
					}
					visitedPath += path
				}
				collectNodes(path.to, type, binding);
			}
		}
		return candidateNodes
	}
	
	def collectNodes(Tree tree, String type, binding){
		return collectNodes(tree.root, type, binding)
	}
	
	def collectNodes(Tree tree, String type){
		collectNodes(tree, type, new Binding())
	}
}
