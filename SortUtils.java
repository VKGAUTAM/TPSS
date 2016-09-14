/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arl.chips.utils;

import com.arl.chips.components.Point;
import java.util.*;

/**
 *
 * @author VKGautam
 */
public class SortUtils {

    public SortUtils() {
    }

    public static void ReverseSort(ArrayList<Double> score) {
        // Reverse Sorting
        Collections.sort(score, new Comparator<Double>() {

            @Override
            public int compare(Double o2, Double o1) {
                return (o1.compareTo(o2));
            }
        });        
    }
    
    public static Map<String, Map<Integer, Integer>> sortMapStrMapByKey(Map<String, Map<Integer, Integer>> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map<Integer, Integer> sortMapByValue(Map<Integer, Integer> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Point, Double> sortMapPDByValue(Map<Point, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Double, Point> sortMapScorePointByKey(Map<Double, Point> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Double, List<Point>> sortMapScoreListPointByKey(Map<Double, List<Point>> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Double, Set<Point>> sortMapScoreSetPointByKey(Map<Double, Set<Point>> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Integer, Double> sortMapIDByValue(Map<Integer, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<String, Map<Integer, Double>> sortMapStrMapIDByKey(Map<String, Map<Integer, Double>> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map<Integer, Double> sortMapIntDouByValue(Map<Integer, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map<Integer, Double> result = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(Integer.parseInt(entry.getKey().toString().trim()), Double.parseDouble(entry.getValue().toString().trim()));
        }
        return result;
    }

    public static Map<String, Double> sortMapStrDouByValue(Map<String, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map<String, Double> result = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey().toString().trim(), (double)entry.getValue());
        }
        return result;
    }

    public static Map<String, Integer> sortMapStrIntByValue(Map<String, Integer> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey().toString().trim(), (int)entry.getValue());
        }
        return result;
    }

    public static Map<String, Map<String, Double>> sortMapStrMapStrDouByKeyValue(Map<String, Map<String, Double>> map) {
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String file = itr.next().toString().trim();
            Map<String, Double> filescore = (Map<String, Double>) map.get(file);
            filescore = sortMapStrDouByValue(filescore);
            map.put(file, filescore);
        }

        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map<Integer, Map<String, Double>> sortMapIntMapStrDouByKeyValue(Map<Integer, Map<String, Double>> map) {
        Iterator<Integer> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            int file = itr.next();
            Map<String, Double> filescore = (Map<String, Double>) map.get(file);
            filescore = sortMapStrDouByValue(filescore);
            map.put(file, filescore);
        }

        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static Map<Integer, Map<Integer, Double>> sortMapIntMapIntDouByKeyValue(Map<Integer, Map<Integer, Double>> map) {
        Iterator<Integer> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            int file = itr.next();
            Map<Integer, Double> filescore = (Map<Integer, Double>) map.get(file);
            filescore = sortMapIntDouByValue(filescore);
            map.put(file, filescore);
        }

        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Map<Integer, Map<Integer, Double>> mapSI = new HashMap<>();
        Point p = new Point(2, 3);
        Map<Integer, Double> mapInner = new HashMap<>();
        mapInner.put(3, 0.2314);
        mapSI.put(2, mapInner);
        
        p = new Point(1, 4);
        mapInner = new HashMap<>();
        mapInner.put(4, 0.8914);
        mapSI.put(1, mapInner);
        
        mapInner = new HashMap<>();
        mapInner.put(7, 0.8914);
        mapSI.put(5, mapInner);
        
        mapInner = new HashMap<>();
        mapInner.put(1, 0.4345);
        mapSI.put(7, mapInner);
        
        mapInner = new HashMap<>();
        mapInner.put(8, 0.1434);
        mapSI.put(4, mapInner);
        
        mapInner = new HashMap<>();
        mapInner.put(7, 0.3142);
        mapSI.put(5, mapInner);
        
        mapInner = new HashMap<>();
        mapInner.put(7, 0.1142);
        mapSI.put(5, mapInner);

        mapSI = SortUtils.sortMapIntMapIntDouByKeyValue(mapSI);
        List<Integer> listi = new LinkedList<>();
        listi.addAll(mapSI.keySet());
        p.setI(listi.get(0));
        
        Map<Integer, Double> mapinner = mapSI.get(p.getI());
        List<Integer> listj = new LinkedList<>();
        listj.addAll(mapinner.keySet());
        p.setJ(listj.get(0));
        
        System.out.println("Chosen Point: (" + p.getI() + ", " + p.getJ() + ")");
        
        PrintUtils.getInstance().printMapIID(mapSI);
    }
}
