package product.tree

class Tree {
    Node root
    String name
}

class Node{
    int id;
    Boolean root=false;
    Path[] froms=[]
    Path[] tos=[]
}
class Path{
    Node from
    Node to
	String type
    RuleChain ruleChain
}
class RuleChain{
    int id
    String name
    Rule[] rules = []

    def permissionPolicy
}

class Rule{
    String name
    String content
    //Map fixedParams
}
