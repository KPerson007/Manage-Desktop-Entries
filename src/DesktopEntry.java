import java.io.*;
import java.util.ArrayList;

/**
 * Created by kevin on 11/12/16.
 */
public class DesktopEntry {
    private File desktopFile;
    boolean parsed;
    private String name;
    private boolean noDisplay;
    private ArrayList<String> categories;
    private String icon;

    public DesktopEntry(String name, File desktopFile, boolean noDisplay, ArrayList<String> categories, String icon)
    {
        this.desktopFile = desktopFile;
        parsed = true;
        this.name = name;
        this.noDisplay = noDisplay;
        this.categories = categories;
        this.icon = icon;
    }

    public DesktopEntry(File desktopFile)
    {
        this.desktopFile = desktopFile;
        parsed = false;
        name = "null";
        noDisplay = false;
        categories = new ArrayList<String>();
        icon = "null";
        try
        {
            //read the desktop file
            FileReader reader = new FileReader(this.desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            boolean named = false;
            String line = null;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.startsWith("Name=") && !named)
                {
                    name = line.substring(line.indexOf("=") + 1);
                    named = true;
                }
                if (line.startsWith("noDisplay"))
                    if (line.contains("true"))
                        noDisplay = true;
                if (line.startsWith("Icon"))
                    icon = line.substring(line.indexOf("=") + 1);
                if (line.startsWith("Categories"))
                {
                    String temp = line.substring(line.indexOf("=") + 1);
                    while (temp.contains(";"))
                    {
                        categories.add(temp.substring(0, temp.indexOf(";")));
                        temp = temp.substring(temp.indexOf(";") + 1);
                    }
                    if (!temp.equals(""))
                        categories.add(temp);
                }
            }
            //close the readers
            bufferedReader.close();
            reader.close();
            parsed = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            parsed = false;
        }
    }

    public void setName (String newName)
    {
        try
        {
            //init file writers
            FileWriter writer = new FileWriter(desktopFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            //read the desktop file
            FileReader reader = new FileReader(desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            boolean named = false;
            String line = null;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.startsWith("Name=") && !named)
                {
                    named = true;
                    writer.write("Name=" + newName);
                }
                else
                {
                    writer.write("ayy");
                }
            }
            //close the readers
            bufferedReader.close();
            reader.close();
            bufferedWriter.close();
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public File getDesktopFile()
    {
        return desktopFile;
    }

    public boolean getParsed()
    {
        return parsed;
    }

    public String getName()
    {
        return name;
    }

    public boolean getNoDisplay()
    {
        return noDisplay;
    }

    public ArrayList<String> getCategories()
    {
        return categories;
    }

    public String getCategoryAt(int index)
    {
        return categories.get(index);
    }

    public int hasCategory(String category) //if the category is found, return the index, otherwise, return -1
    {
        if (categories.contains(category))
            return categories.indexOf(category);
        else
            return -1;
    }

    public String getIcon()
    {
        return icon;
    }

    @Override
    public String toString()
    {
        String theCategories = "";
        for (int i = 0; i < categories.size(); i++)
        {
            theCategories = theCategories + categories.get(i);
            if (i != categories.size() - 1)
                theCategories = theCategories + ", ";
        }
        return "DesktopEntry(" + desktopFile + ", " + parsed + ", " + name + ", " + noDisplay + ", {" + theCategories + "}, " + icon + ")";
    }
}
