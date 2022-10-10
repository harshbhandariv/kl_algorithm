import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import graph.Graph;
import graph.Vertex;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int e = s.nextInt();
        Graph<Integer> graph = new Graph<>(false);
        for (int i = 0; i < e; i++) {
            graph.addEdge(s.nextInt(), s.nextInt());
        }
        Set<Vertex<Integer>> part1 = new HashSet<>();
        Set<Vertex<Integer>> part2 = new HashSet<>();
        Set<Vertex<Integer>> lock_part1 = new HashSet<>();
        Set<Vertex<Integer>> lock_part2 = new HashSet<>();
        for (int i = 0; i < n / 2; i++) {
            part1.add(graph.getVertex(s.nextInt()));
        }
        for (int i = 0; i < n / 2; i++) {
            part2.add(graph.getVertex(s.nextInt()));
        }
        int cut_size = 0;
        for (Vertex<Integer> vertex : part1) {
            List<Vertex<Integer>> list = vertex.getAdjacentVertexes();
            for (Vertex<Integer> adjVertex : list) {
                if (part2.contains(adjVertex)) {
                    cut_size += 1;
                }
            }
        }
        System.out.println("Graph");
        System.out.println(graph);
        System.out.println("Partition 1: " + part1);
        System.out.println("Partition 2: " + part2);
        System.out.println("Initial cut size: " + cut_size);
        // Iteration 1
        int i = 0;
        while (true) {
            i++;
            System.out.println("Iteration: " + i);
            int highestCost = Integer.MIN_VALUE;
            long[] hightestCostVertices = new long[2];
            for (Vertex<Integer> vertex1 : part1) {
                if (lock_part1.contains(vertex1))
                    continue;
                for (Vertex<Integer> vertex2 : part2) {
                    if (lock_part2.contains(vertex2))
                        continue;
                    int ga = getInternalAndExternalEdgeCountDiffernce(vertex1, part1);
                    int gb = getInternalAndExternalEdgeCountDiffernce(vertex2, part2);
                    int cab = checkEdgeExists(vertex1, vertex2);
                    int gab = ga + gb - 2 * cab;
                    System.out.println("g" + vertex1.getId() + "_" + vertex2.getId() + " = " + gab);
                    if (gab > highestCost) {
                        highestCost = gab;
                        hightestCostVertices[0] = vertex1.getId();
                        hightestCostVertices[1] = vertex2.getId();
                    }
                }
            }
            System.out.println("Swapping: (" + hightestCostVertices[0] + ", " + hightestCostVertices[1] + ")");
            part1.remove(graph.getVertex(hightestCostVertices[0]));
            part1.add(graph.getVertex(hightestCostVertices[1]));
            part2.remove(graph.getVertex(hightestCostVertices[1]));
            part2.add(graph.getVertex(hightestCostVertices[0]));
            int new_cut_size = 0;
            for (Vertex<Integer> vertex : part1) {
                List<Vertex<Integer>> list = vertex.getAdjacentVertexes();
                for (Vertex<Integer> adjVertex : list) {
                    if (part2.contains(adjVertex)) {
                        new_cut_size += 1;
                    }
                }
            }
            System.out.println("Paritions after iteration " + i);
            System.out.println("Partition 1: " + part1);
            System.out.println("Partition 2: " + part2);
            System.out.println("New cut size: " + new_cut_size);
            if (new_cut_size >= cut_size) {
                System.out.println("Cut size not improved, reverting back and stopping iterations");
                part1.add(graph.getVertex(hightestCostVertices[0]));
                part1.remove(graph.getVertex(hightestCostVertices[1]));
                part2.add(graph.getVertex(hightestCostVertices[1]));
                part2.remove(graph.getVertex(hightestCostVertices[0]));
                break;
            } else {
                cut_size = new_cut_size;
                lock_part1.add(graph.getVertex(hightestCostVertices[1]));
                lock_part2.add(graph.getVertex(hightestCostVertices[0]));
            }
        }
        System.out.println("Final Paritions: ");
        System.out.println("Partition 1: " + part1);
        System.out.println("Partition 2: " + part2);
        s.close();
    }

    static int getInternalAndExternalEdgeCountDiffernce(Vertex<Integer> vertex, Set<Vertex<Integer>> part) {
        int count = 0;
        for (Vertex<Integer> adjVertex : vertex.getAdjacentVertexes()) {
            if (part.contains(adjVertex)) {
                count--;
            } else {
                count++;
            }
        }
        return count;
    }

    static int checkEdgeExists(Vertex<Integer> vertex1, Vertex<Integer> vertex2) {
        for (Vertex<Integer> adjVertex : vertex1.getAdjacentVertexes()) {
            if (adjVertex == vertex2) {
                return 1;
            }
        }
        return 0;
    }
}
