package product.tree;


public class TreeBuilder {
    static groovy.util.Node getXmlDoc(String xml){
        return  new XmlParser().parseText(xml);
    }
    
    static Tree generateTree(groovy.util.Node xmlTree){
        Tree tree= new Tree();
        def nodes=[] 
        
        xmlTree.node.each {nodes+=convertNode(it)}
        
        assamblePath(nodes, xmlTree.path)
        
        def candidates = nodes.findAll{it.root.is(true)}
        if(candidates.size() != 1){
            throw new IllegalArgumentException("None or more than one root node found. Initialize Tree Builder failed.")
        }
        tree.root=candidates[0];
        
        return tree
    }
    
    private static Node convertNode(groovy.util.Node xmlNode){
        product.tree.Node node = new  product.tree.Node()
        node.id = xmlNode.@id
        node.root=xmlNode.@root
        return node
    }
    
    private static assamblePath(nodes, xmlPaths){
        xmlPaths.each {xmlPath->
            Path path = new Path();
            
            path.from = nodes.findAll{it.id+"" == xmlPath.from.@id[0]}
            path.from.tos += path

            path.to = nodes.findAll{it.id+"" == xmlPath.to.@id[0]}
            path.to.froms += path
            
            path.ruleChain = convertRuleChain(xmlPath.chain)
        }
    }
    
    private static convertRuleChain(xmlChain){
        RuleChain chain = new RuleChain()
        chain.id=xmlChain.@id[0]
        chain.name=xmlChain.@name[0]
        
        chain.rules = convertRules(xmlChain.rule)
        
        def  checkRules = Closure checkRules() {
            it.every {evaluate(it.content)}
        }
        chain.permissionPolicy = checkRules(chain.rules);
        
    }
    private static convertRules(xmlChain){
        def rules = []
       xmlChain.rule.each{
           Rule rule = new Rule()
           rule.content= it.@name[0]
           rule.content=it.text()
           rules+=rule
       }
       return rules
    }
    
}

