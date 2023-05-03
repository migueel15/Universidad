package gui;

import java.awt.event.ActionListener;

public interface ViewRank {
    public enum Command{ CLICK, CREATE, BYNAME, BYRANK, SIMULATE, SWITCH };
    public void controler(ActionListener ctr);
    public String getFileName();
    public String getSiteName();
    public void addOutputLine(String line);
    public void setError(String msg);
}
