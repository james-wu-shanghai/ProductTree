package product.tree

def builder = new NodeBuilder()
def tree = builder.tree (name:'test tree'){
    root{
        froms:null
        tos:Path{
            from:root
            to:null
            ruleChain{
                rules:{
                    rule(name:'test rule',content:'return true',fixedParame:null);
                }
            }
        }
    }
}
println tree