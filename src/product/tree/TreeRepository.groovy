package product.tree

class TreeRepository {
    def treeRep = [:]
    
    def TreeRepository(List<String> xmlTrees, TreeBuilder treeBuilder){
        
        xmlTrees.each{
            def tree = treeBuilder.generateTree(it)
            treeRep[tree.name] = tree
        }
    }
    
    Tree getTree(String name){
        return treeRep[name]
    }
    
    def Set getTreeNames(){
        return treeRep.keySet()
    }
}