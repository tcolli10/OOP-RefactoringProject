import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class AStarPathingStrategy implements PathingStrategy{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        List<Point> path = new ArrayList<>();
        Node startNode = new Node(start, end, start, 0);
        HashSet<Point> closedList = new HashSet<>();
        Comparator<Node> nodeComparator = Comparator.comparingInt(n -> n.getTotalDistance());
        PriorityQueue<Node> openList = new PriorityQueue<>( 100,  nodeComparator );
        openList.add(startNode);

        while (!openList.isEmpty()){
            Node currentNode = openList.remove();

            if (withinReach.test(currentNode.getPosition(), end)){
                while (currentNode.getPriorNode() != null ){
                    path.add(currentNode.getPosition());
                    currentNode = currentNode.getPriorNode();
                }
                Collections.reverse(path);
                return path;
            }

            List<Point> neighbors = potentialNeighbors.apply(currentNode.getPosition())
                    .filter(canPassThrough)
                    .filter(pt ->
                            !pt.equals(start)
                                    && !closedList.contains(pt))


                    .collect(Collectors.toList());
            for(Point p : neighbors){
                Node neighbor = new Node(p, end, start, currentNode.getDistanceFromStart() + 1);
                neighbor.setPriorNode(currentNode);

                boolean alreadyIn = false;

                for(Node cur : openList){
                    if (p.equals(cur.getPosition()) && neighbor.getDistanceFromStart() < cur.getDistanceFromStart()){
                        cur.setDistanceFromStart(neighbor.getDistanceFromStart());
                        cur.setPriorNode(neighbor.getPriorNode());
                    }
                    if (cur.getPosition().equals(neighbor.getPosition())){
                        alreadyIn = true;
                    }
                }
//                boolean alreadyIn = false;
//                for(Node element : openList){
//                    if (element.getPosition().equals(neighbor.getPosition())){
//                        alreadyIn = true;
//                    }
//                }
                if(alreadyIn == false){
                    openList.add(neighbor);
                }
            }
            closedList.add(currentNode.getPosition());
        }
        return path;
    }

    public boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

}
