package product.tree

class Tree {
    Node root
    String name
}

class Node{
    int id;
    Boolean root=false
    NodeOffer nodeOffer
    Path[] froms=[]
    Path[] tos=[]
}
class NodeOffer{
    String code
    String type
}
class Path{
    Node from
    Node to
	String type
    RuleChain ruleChain
}
class RuleChain{
    String name
    Rule[] rules = []
    boolean pass=true
    String type="AND"
    def permissionPolicy
}

class Rule implements Cloneable{
    String name
    String content
    boolean pass=true
	def script
}
