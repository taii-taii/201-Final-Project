package test;

import java.util.LinkedList;


public class TestMain {
	
	public static void main(String args[]) {
		diff_match_patch dmp = new diff_match_patch();
		String clientText1 = "Today the sun is bright and shiny";
		String clientText2 = "Today is raining cats and dogs";

		//Compute and make patches by diff'ing text1 and text2
		LinkedList<test.diff_match_patch.Patch> list_of_patches = dmp.patch_make(clientText2,clientText1);
		String testout = dmp.patch_toText(list_of_patches);
		System.out.println(testout);
		//Apply patches
		Object[] textString = dmp.patch_apply(list_of_patches, clientText2);
		System.out.println(textString[0]);
	}

	
}
