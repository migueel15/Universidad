package rank;

public class SiteExtended extends Site{
  private boolean valid;
  public SiteExtended(String name){
    super(name);
    valid = true;
  }
  public void setValid(boolean b){
    this.valid = b;
  }
  public boolean isValid(){
    return valid;
  }

  @Override
  public String toString() {
    String defaultValue = super.toString();
    if(!valid){
      defaultValue = defaultValue.concat("*");
    }

    return defaultValue;
  }
}
