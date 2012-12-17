package product.tree

class Tree {
    Node root
    String name
}

class Node{
    Path[] froms
    Path[] tos
}
class Path{
    Node from
    Node to
    RuleChain ruleChain
}
class RuleChain{
    Rule[] rules

    def permissionPolicy
}

class Rule{
    String name
    String content
    Map fixedParams
}

