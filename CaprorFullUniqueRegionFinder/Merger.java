package core;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Merger {

	public static TreeMap<Integer, Integer> perfectmerge(Map<Integer, Integer> map){

		Map<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(map);
		int[] startPos = new int[sortedMap.size()];
		int[] endPos = new int[sortedMap.size()];

		Iterator it = sortedMap.entrySet().iterator();
		int indexPos = 0;
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next();
			startPos[indexPos] = (int) pair.getKey();
			endPos[indexPos] = (int) pair.getValue();
			indexPos++;
		}

		//create merged regions
		TreeMap<Integer, Integer> mergedMap = new TreeMap<Integer, Integer>();

		indexPos = 0;

		while (indexPos < startPos.length){
			int currentStart = startPos[indexPos];
			int currentEnd = endPos[indexPos];

			if (indexPos < startPos.length -1){
				while (currentEnd >= startPos[indexPos+1]){
					currentEnd = endPos[indexPos+1];
					indexPos++;
					if (indexPos >= startPos.length -1){
						currentEnd = endPos[indexPos];
						break;
					}
				}
			}

			mergedMap.put(currentStart, currentEnd);

			if (indexPos < startPos.length) {
				indexPos++;
			}
		}
		return mergedMap;
	}


	public static TreeMap<Integer, Integer> relaxedmerge(Map<Integer, Integer> map, int gapsize){
		//Sort map
		Map<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(map);
		
		//put keys into one array and values into another array
		int[] startpos = new int[sortedMap.size()];
		int[] endpos = new int[sortedMap.size()];

		Iterator it = sortedMap.entrySet().iterator();
		int indexPos = 0;
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next();
			startpos[indexPos] = (int) pair.getKey();
			endpos[indexPos] = (int) pair.getValue();
			indexPos++;
		}

		//create merged regions
		TreeMap<Integer, Integer> mergedMap = new TreeMap<Integer, Integer>();

		indexPos = 0;

		while (indexPos < startpos.length){
			int currentStart = startpos[indexPos];
			int currentEnd = endpos[indexPos];

			if (indexPos < startpos.length -1){
				while ((currentEnd + gapsize) >= startpos[indexPos+1]){
					currentEnd = endpos[indexPos+1];
					indexPos++;
					if (indexPos >= startpos.length -1){
						currentEnd = endpos[indexPos];
						break;
					}
				}
			}

			mergedMap.put(currentStart, currentEnd);

			if (indexPos < startpos.length) {
				indexPos++;
			}
		}

		return mergedMap;
	}

}
