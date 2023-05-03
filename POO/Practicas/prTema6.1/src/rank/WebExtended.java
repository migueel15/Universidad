package rank;

import java.util.Random;

public class WebExtended extends Web{
  public WebExtended(){
    super();
  }

  @Override
  protected void addSiteWithName(String name) {
    SiteExtended site = new SiteExtended(name);
    addSite(site);
  }
  @Override
  protected void distribute(Site site, double prize) {
    SiteExtended siteE = (SiteExtended) site;
    if(siteE.isValid()){
      super.distribute(siteE, prize);
    }
  }


  public void switchSiteWithName(String name){
    SiteExtended site = (SiteExtended) getSite(name);
    site.setValid(!site.isValid());
  }
}
