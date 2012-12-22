package product.tree

String testXml = 
'''<tree name="test">
    <node id="1" root="true"/>
    <node id="2"/>
    <node id="3"/>
	<node id="4"/>
    <path type="purchaseOption">
        <from id="1"/>
        <to id="2"/>
        <chain id="1" name="test chain1"/>
    </path>
    <path type="purchaseOption">
        <from id="2"/>
        <to id="3"/>
        <chain id="2" name="test chain2"/>
    </path>
    <path type="autoProvision">
        <from id="1"/>
        <to id="3"/>
        <chain id="3" name="test chain3"/>
    </path>
	<path type="autoProvision">
        <from id="3"/>
        <to id="4"/>
        <chain id="4" name="test chain4"/>
    </path>
</tree>
'''

String xmlChains= 
'''<chains>
	<chain id="1" name="test chain1">
		<rule id="1" name="testrule1"/>
		<rule id="2" name="testrule2"/>
	</chain>
	<chain id="2" name="test chain2">
		<rule id="1" name="testrule1"/>
		<rule id="3" name="testrule3"/>
	</chain>
	<chain id="3" name="test chain3">
		<rule id="2" name="testrule2"/>
		<rule id="3" name="testrule3"/>
	</chain>
	<chain id="4" name="test chain4">
		<rule id="1" name="testrule2"/>
		<rule id="4" name="testrule4"/>
	</chain>
</chains>'''
String xmlRules=
'''<rules>
	<rule id="1" name="testrule1">
		true
	</rule>
	<rule id="2" name="testrule2">
		true
	</rule>
	<rule id="3" name="testrule3">
	<![CDATA[
		return testVar=='1234'
	]]>
	</rule>
	<rule id="4" name="testrule4">
	<![CDATA[
		return accountinfo != null
	]]>
	</rule>
</rules>
'''

def ruleChainRep = new RuleChainRepository(xmlChains, xmlRules)
assert 4 == ruleChainRep.chainsMap.size()

def tree = new TreeBuilder(ruleChainRep).generateTree(testXml)
assert null != tree

def binding=new Binding(testVar:'1234')
assert [2] == new TreeIterator().collectNodes(tree, 'purchaseOption').id.sort()

assert [3] == new  TreeIterator().collectNodes(tree, 'autoProvision', binding).id.sort()