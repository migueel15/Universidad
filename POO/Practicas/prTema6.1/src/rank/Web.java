package rank;

import java.util.*;

public class Web {
  private static final double THRESHOLD = 1E-5;
  protected Set<Site> sites;
  private Set<Link> links;
  public Web(){
    this.sites = new HashSet<>();
    this.links = new HashSet<>();
  }
  protected void addSite(Site site){
    sites.add(site);
  }
  protected void addSiteWithName(String name){
    sites.add(new Site(name));
  }
  public void addLink(String dataLink){
    String[] data = dataLink.split("\\s*->\\s*");
    if(data.length != 2){
      throw new IllegalArgumentException("Error en datalink: " + dataLink);
    }
    String origin = data[0];
    String linked = data[1];
    addSiteWithName(origin);
    addSiteWithName(linked);
    links.add(new Link(origin, linked));
  }
  public Site getSite(String name){
    boolean exist = false;
    Site siteReturned = null;
    Iterator<Site> it = sites.iterator();
    while(it.hasNext() && !exist){
      Site actualSite = it.next();
      if(actualSite.equals(new Site(name))){
        exist = true;
        siteReturned = actualSite;
      }
    }
    if(!exist){
      throw new NoSuchElementException("Pagina " + name + " no encontrada");
    }
    return siteReturned;
  }
  public Set<String> getNames(){
    Set<String> names = new HashSet<>();
    for(Site site : sites){
      names.add(site.getName());
    }
    return names;
  }
  private Set<Site> getSitesLinkedFrom(Site pagina){
    Set<Site> linkedPages = new HashSet<>();
    for(Link link : links){
      if(link.getOrigin().equalsIgnoreCase(pagina.getName())){
        linkedPages.add(getSite(link.getLinked()));
      }
    }
    return linkedPages;
  }
  protected void distribute(Site site, double prize){
    if(prize >= THRESHOLD){
      double halfPrice = prize/2;
      site.addRank(halfPrice);
      Set<Site> linkedSites = getSitesLinkedFrom(site);
      if(linkedSites.size() > 0){
        for(Site actualSite : linkedSites){
          distribute(actualSite,halfPrice/linkedSites.size());
        }
      }
    }
  }
  public void click(String name){
    Site site = getSite(name);
    distribute(site,1);
  }
  public void simulateClick(int numClick){
    if(sites.size() > 0){
      int contador = numClick;
      Random alea = new Random(1);
      Object[] sitesA = sites.toArray();
      while (contador > 0){
        Site site1 = (Site) sitesA[alea.nextInt(sitesA.length)];
        click(site1.getName());
        contador--;
      }
    }
  }
  public SortedSet<Site> getSitesByName(){
    return new TreeSet<>(sites);
  }
  public SortedSet<Site> getSitesByRank(){
    Comparator<Site>comparator = new RankOrder();
    SortedSet<Site> orderedRank = new TreeSet<>(comparator);
    orderedRank.addAll(sites);
    return orderedRank;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(", ","(", ")");
    sj.add(sites.toString());
    sj.add(links.toString());
    return String.format("Web"+ sj.toString());
  }
}
