import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 11/12/16.
 */
public class Main {
    public static void main (String[] args)
    {
        File mainDir = new File("/usr/share/applications");
        if (mainDir.exists() && mainDir.isDirectory())
        {
            //get every file in /usr/share/applications
            List<File> desktopEntriesTemp = Arrays.asList(mainDir.listFiles());
            ArrayList<File> desktopEntryFiles = new ArrayList<File>();
            //check every file in /usr/share/applications to see if its's a .desktop file, and keep all .desktops in another ArrayList
            for (int i = 0; i < desktopEntriesTemp.size(); i++)
                if (desktopEntriesTemp.get(i).toString().endsWith(".desktop"))
                {
                    desktopEntryFiles.add(desktopEntriesTemp.get(i));
                    System.out.println(desktopEntriesTemp.get(i));
                }
            System.out.println(desktopEntryFiles.size());
            //create some DesktopEntries from all of the .desktop files
            ArrayList<DesktopEntry> desktopEntries = new ArrayList<DesktopEntry>();
            for (int i = 0; i < desktopEntryFiles.size(); i++)
            {
                desktopEntries.add(new DesktopEntry(desktopEntryFiles.get(i)));
                System.out.println(desktopEntries.get(i).toString());
            }
            //desktopEntries.get(1).setName("Emailsss");
            System.out.println("\n" + desktopEntries.get(1).toString());
        }
        else
            System.out.println("Error! Directory " + mainDir.getPath() + " does not exist!");
    }
}
