import java.util.*;
import java.io.*;

/**
 * A program to implement Dijkstra's shortest path algorithm on a graph of airports
 * Author: Jeremy Genovese
 * Class: COSC 314 Fall 2021
 * Version 0.1
 */
public class Dijkstra {
    private static ArrayList <Node> unvisited = new ArrayList<>();
    private static ArrayList <Node> visited = new ArrayList<>();
    private static ArrayList <String> codes = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> distances = new ArrayList<ArrayList<Integer>>();

    public static void main (String[] args){
        File codeFile = new File("airport_codes.txt");
        File distanceFile = new File("distance_matrix.txt");
        Scanner codesIn = null;
        Scanner distancesIn = null;
        Scanner input = new Scanner (System.in);
        Node currentNode = null;
        String start = "";
        String end = "";
        boolean endFlag = false;

        // create scanners to traverse the input files
        try {
            codesIn = new Scanner(codeFile);
            distancesIn = new Scanner((distanceFile));
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        // generate the list of airport codes
        while (codesIn.hasNext()){
            codes.add(codesIn.next());
        }

        // generate the adjacency matrix
        for (int i = 0; i < codes.size(); i++){
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < codes.size(); j++){
                temp.add(distancesIn.nextInt());
            }
            distances.add(temp);
        }

        // populate the unvisited queue
        for (int i = 0; i < codes.size(); i ++){
            Node temp = new Node (codes.get(i));
            unvisited.add(temp);
        }

        // always close your scanners
        codesIn.close();
        distancesIn.close();

        // get the airport codes from the user
        System.out.print("\nStart: ");
        start = input.nextLine();
        System.out.print("\nEnd: ");
        end = input.nextLine();

        // visit the first node
        currentNode = unvisited.get(codes.indexOf(start));
        currentNode.setDistance(0);
        visit(currentNode);
        unvisited.remove(currentNode);
        visited.add(currentNode);

        // visit the rest of the nodes
        while(!endFlag) {
            currentNode = getNext(unvisited);
            visit(currentNode);
            unvisited.remove(currentNode);
            visited.add(currentNode);
            if (currentNode.getCode().equals(end)){
                endFlag = true;
            }
        }


        // output results to the user on the console
        System.out.println("The shortest path between " + start + " and " + end + " is " + currentNode.getDistance() + " miles.");
        System.out.println("The shortest path is:");
        ArrayList path = getPath(visited, start, end);
        for (int i = path.size() - 1; i > 0; i--){
            System.out.print(path.get(i) + " --> ");
        }
        System.out.print(end);

        // close the keyboard scanner
        input.close();
    }

    // visit a Node in the graph. Process the corresponding row in the adjacency matrix
    // and adjust distances as necessary.
    // if the distance is adjusted, set the predecessor node to the node being processed
    public static void visit (Node current){
        int rowNum = codes.indexOf(current.getCode());
        ArrayList <Integer> row = distances.get(rowNum);
        for(Node temp : unvisited){
            if ((current.getDistance() + row.get(codes.indexOf(temp.getCode()))) < temp.getDistance()) {
                temp.setDistance(current.getDistance() + row.get(codes.indexOf(temp.getCode())));
                temp.setPrevNode(current);
            }
        }
    }

    // get the smallest element in the list (next node to visit)
    public static Node getNext (ArrayList<Node> list){
        Node temp = list.get(0);
        for (Node elm : list){
            if (elm.compareTo(temp) < 1){
                temp = elm;
            }
        }
        return temp;
    }

    // generate the path by tracing each node to its predecessor
    public static ArrayList<String> getPath(ArrayList<Node> n, String st, String en){
        Node e = null;
        Node s = null;
        Node temp = null;
        ArrayList<String> list = new ArrayList<>();
        for (Node elm : n){
            if (elm.getCode().equals(en)){
                temp = e = elm;
            }
            else if (elm.getCode().equals(st)){
                s = elm;
            }
        }
        while (temp != s){
            list.add(temp.getCode());
            temp = temp.getPrevNode();
        }
        list.add(s.getCode());
        return list;
    }


    // a class to represent the vertices in a graph
    public static class Node implements Comparable<Node>{
        private String code;
        private Integer distance;
        private Node prevNode;

        public Node(){
            code = "";
            distance = Integer.MAX_VALUE;
            prevNode = null;
        }

        public Node(String c){
            code = c;
            distance = Integer.MAX_VALUE;
            prevNode = null;
        }

        public Node (String c, Integer d){
            code = c;
            distance = d;
            prevNode = null;
        }

        public void setDistance(Integer d){
            distance = d;
        }

        public void setPrevNode(Node n){
            prevNode = n;
        }

        public void setCode(String c){
            code = c;
        }

        public Integer getDistance(){
            return distance;
        }

        public String getCode(){
            return code;
        }

        public Node getPrevNode(){
            return prevNode;
        }

        @Override
        public String toString(){
            return (code + " " + distance + " " + prevNode);
        }

        @Override
        public int compareTo(Node other) {
            if (this.getDistance().equals(other.getDistance())){
                return this.getCode().compareTo(other.getCode());
            }
            else {
                return Integer.compare(this.getDistance(), other.getDistance());
            }
        }
    }
}
