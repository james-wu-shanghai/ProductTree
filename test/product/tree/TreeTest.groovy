package product.tree

String testXml = 
'''<tree name='test'>
    <node id='1' root='true'/>
    <node id='2'/>
    <node id='3'/>
	<node id='4'/>
    <path type='purchaseOption'>
        <from id='1'/>
        <to id='2'/>
        <chain id='1' name="test chain1">
            <rule id='1' name='test rule1'>
                true
            </rule>
            <rule id='2' name='test rule2'>
                true
            </rule>
        </chain>
    </path>
    <path type='purchaseOption'>
        <from id='2'/>
        <to id='3'/>
        <chain id='2' name="test chain2">
            <rule id='1' name='test rule1'>
                true
            </rule>
            <rule id='3' name='test rule3'>
                true
            </rule>
        </chain>
    </path>
    <path type='autoProvision'>
        <from id='1'/>
        <to id='3'/>
        <chain id='3' name="test chain3">
            <rule id='2' name='test rule2'>
			<![CDATA[
               return true
			]]>
            </rule>
            <rule id='3' name='test rule3'>
                return testVar=='1234'
            </rule>
        </chain>
    </path>
	<path type='autoProvision'>
        <from id='3'/>
        <to id='4'/>
        <chain id='3' name="test chain3">
            <rule id='2' name='test rule2'>
			<![CDATA[
               return true
			]]>
            </rule>
            <rule id='3' name='test rule3'>
                return testVar=='1234'
            </rule>
        </chain>
    </path>
</tree>
'''

def tree = new TreeBuilder().generateTree(testXml)
assert null != tree

def binding=new Binding(testVar:'1234')
assert [2,3] == new TreeIterator().collectNodes(tree, 'purchaseOption').id.sort()

assert [3,4] == new  TreeIterator().collectNodes(tree, 'autoProvision', binding).id.sort()