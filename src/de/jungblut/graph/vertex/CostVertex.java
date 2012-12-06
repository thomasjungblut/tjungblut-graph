package de.jungblut.graph.vertex;

/**
 * Cost vertex. This is an extend to the basic vertex and adds a cost.
 * 
 * @author thomas.jungblut
 * 
 */
public final class CostVertex extends BasicVertex {

  private final int cost;

  public CostVertex(int id, int cost) {
    super(id);
    this.cost = cost;
  }

  public int getCost() {
    return cost;
  }

  @Override
  public String toString() {
    return "CostVertex [getCost()=" + this.getCost() + ", getVertexId()="
        + this.getVertexId() + "]";
  }

}
