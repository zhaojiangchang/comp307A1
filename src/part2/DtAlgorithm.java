package part2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dc.ReadFile.Instance;

public class DtAlgorithm {
//	private List<ReadFile.Instance>allInstances;
//	private List<String>allAttrs;


//	public DtAlgorithm(List<ReadFile.Instance>allInstances, List<String>allAttrs){
//		this.allInstances = allInstances;
//		this.allAttrs = allAttrs;
//	}

	public Node buildTree(List<ReadFile.Instance>instances, List<String>attrs){
		String bestAtt = "";
		List<ReadFile.Instance>bestInstsTrue = new ArrayList<ReadFile.Instance>();
		List<ReadFile.Instance>bestInstsFalse = new ArrayList<ReadFile.Instance>();
		if(instances.size()==0) {
			Node leaf = new DTLeaf(0, 0.2) ;
			return leaf;
		}
		if(isPure(instances)){
			Node leaf =  new DTLeaf(instances.get(0).getCategory(),1);
		}
		if(attrs.size()==0){
			Node leaf =  new DTLeaf(majorityClass(instances), majorityProb(instances));
			return leaf;
		}
		else{
			List<Double>ImpuritiesRates = new ArrayList<Double>();
			for(int i = 0; i<attrs.size(); i++){
				List<ReadFile.Instance>trueIns = new ArrayList<ReadFile.Instance>();
				List<ReadFile.Instance>falseIns = new ArrayList<ReadFile.Instance>();
				for(int j= 0; j<instances.size(); j++){
					if(instances.get(j).getAtt(i)) trueIns.add(instances.get(j));
					else falseIns.add(instances.get(j));
				}
				double impurity = calPurity(trueIns,falseIns);
				ImpuritiesRates.add(impurity);
				if(ImpuritiesRates.size()==1){
					bestAtt = attrs.get(i);
					bestInstsTrue = trueIns;
					bestInstsFalse = falseIns;
				}else{
					Collections.sort(ImpuritiesRates);
					if(ImpuritiesRates.get(0)==impurity){
						bestAtt = attrs.get(i);
						bestInstsTrue = trueIns;
						bestInstsFalse = falseIns;
					}
				}
			}
			for(int a = 0; a<attrs.size(); a++){
				if(attrs.get(a).equals(bestAtt)){
					attrs.remove(a);
				}
			}
			Node left = buildTree(bestInstsTrue, attrs);
			Node right = buildTree(bestInstsFalse, attrs);
			Node node = new DTNode(bestAtt, left, right); 
			return node;
		}	
	}

	private double majorityProb(List<ReadFile.Instance> instances) {
		// TODO Auto-generated method stub
		int majority = 0;
		for(int i = 0; i<instances.size(); i++){
			if(instances.get(i).getCategory()==0)
				majority++;
		}
		if(majority>instances.size()/2)
			return (double)majority/instances.size();
		else if(majority<instances.size()/2)
			return 1 - (double)majority/instances.size();
		return 0.5;
	}

	private int majorityClass(List<ReadFile.Instance> instances) {
		// TODO Auto-generated method stub
		int majority = 0;
		for(int i = 0; i<instances.size(); i++){
			if(instances.get(i).getCategory()==0)
				majority++;
		}
		if((instances.size()-majority)<instances.size()/2)
			return 0;
		else if((instances.size()-majority)>instances.size()/2)
			return 1;
		return (int)(Math.random() * 2);
	}

	private boolean isPure(List<ReadFile.Instance> instances) {
		// TODO Auto-generated method stub
		for(int i = 1; i<instances.size(); i++){
			if(instances.get(i-1).getCategory()!=instances.get(i).getCategory()){
				return false;
			}
		}
		return true;
	}

	private double calPurity(List<ReadFile.Instance> trueIns, List<ReadFile.Instance> falseIns) {
		// TODO Auto-generated method stub
		int totalSize = trueIns.size()+falseIns.size();
		int numTrueLiveClass = numCat(trueIns);
		int numTrueDieClass = trueIns.size()-numTrueLiveClass;
		int numFalseLiveClass = numCat(falseIns);
		int numFalseDieClass = falseIns.size() - numFalseLiveClass;
		return ((double)trueIns.size()/totalSize)*(((double)numTrueLiveClass/trueIns.size())*((double)numTrueDieClass/trueIns.size()))
				+ ((double)falseIns.size()/totalSize)*(((double)numFalseLiveClass/falseIns.size())*((double)numFalseDieClass/falseIns.size()));
	}
	private int numCat(List<ReadFile.Instance>ins){
		int num = 0;
		for(int i = 0; i<ins.size(); i++){
			if(ins.get(i).getCategory()==0) num++;
		}
		return num;
	}

}