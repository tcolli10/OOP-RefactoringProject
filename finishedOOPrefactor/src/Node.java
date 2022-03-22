public class Node {

    private Point position;
    private Point startPoint;
    private int hueristicDistance;
    private int distanceFromStart;
    private int totalDistance;
    private Node priorNode;
    private Point destination;


    public Node(Point position, Point destination, Point startPoint, int distanceFromStart){
        this.position = position;
        this.destination = destination;
        this.startPoint = startPoint;
        this.hueristicDistance = computeHueristic();
        this.distanceFromStart = distanceFromStart;
        this.totalDistance = this.hueristicDistance + this.distanceFromStart;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }


    public Point getPosition(){return this.position;}

    public Node getPriorNode() {
        return priorNode;
    }

    public void setPriorNode(Node priorNode) {
        this.priorNode = priorNode;
    }

    public int computeHueristic(){
        int xDistance = Math.abs(this.destination.getX() - this.position.getX());
        int yDistance = Math.abs(this.destination.getY() - this.position.getY());
        return xDistance + yDistance;
    }

//    @Override
//    public boolean equals(Object other){
//        return other instanceof Node
//                && this.position.equals(((Node) other).position);
//    }

    @Override
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        if (this == other){
            return true;
        }
        if(other.getClass() != this.getClass()){
            return false;
        }
        Node otherNode = (Node) other;

        return this.position.equals(otherNode.position);
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + position.getX();
        result = result * 31 + position.getY();
        return result;
    }

//
//    public int computeDistanceFromStart(){
//        int xDistance = Math.abs(this.startPoint.getX() - this.position.getX());
//        int yDistance = Math.abs(this.startPoint.getY() - this.position.getY());
//        return xDistance + yDistance;
//    }



}
