import java.io.*;
import java.util.ArrayList;

/**
 * Created by kevin on 11/12/16.
 */
public class DesktopEntry {
    //TODO: Add support for more fields, such as "Comment", and maybe add support for renaming desktop entries altogether
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
                if (line.startsWith("NoDisplay"))
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
        final String constant = "Name=";
        try
        {
            String writeBuffer = "";
            //read the desktop file
            FileReader reader = new FileReader(desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);


            boolean written = false;
            String line = null;
            boolean firstLine = true;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line);
                if (!firstLine)
                    writeBuffer = writeBuffer + "\n";
                if (line.startsWith(constant) && !written)
                {
                    written = true;
                    writeBuffer = writeBuffer + constant + newName;
                }
                else
                {
                    writeBuffer = writeBuffer + line;
                }
                firstLine = false;
            }
            //close the readers
            bufferedReader.close();
            reader.close();
            System.out.println("Write buffer:");
            System.out.println("\n WRITE BUFFER \n" + writeBuffer);
            //init file writer
            FileWriter writer = new FileWriter(desktopFile);
            //write the buffer
            if (writeBuffer.startsWith("[Desktop Entry]") == false)
                writeBuffer = "[Desktop Entry]" + writeBuffer;
            if (!written)
            {
                writer.write("[Desktop Entry]\n" + constant + newName);
                writer.append(writeBuffer.substring(writeBuffer.indexOf("[Desktop Entry]") + 15));
            }
            else
                writer.write(writeBuffer);
            writer.close();
            name = newName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setNoDisplay (boolean newNoDisplay)
    {
        final String constant = "NoDisplay";
        try
        {
            String writeBuffer = "";
            //read the desktop file
            FileReader reader = new FileReader(desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);


            boolean written = false;
            String line = null;
            boolean firstLine = true;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                if (!firstLine)
                    writeBuffer = writeBuffer + "\n";
                if (line.startsWith(constant) && !written)
                {
                    written = true;
                    writeBuffer = writeBuffer + constant + "=" + newNoDisplay;
                }
                else
                {
                    writeBuffer = writeBuffer + line;
                }
                firstLine = false;
            }
            //close the readers
            bufferedReader.close();
            reader.close();

            //init file writer
            FileWriter writer = new FileWriter(desktopFile);
            //write the buffer
            if (!written)
            {
                writer.write("[Desktop Entry]\n" + constant + "=" + newNoDisplay);
                writer.append(writeBuffer.substring(writeBuffer.indexOf("[Desktop Entry]") + 15));
            }
            else
                writer.write(writeBuffer);
            writer.close();

            noDisplay = newNoDisplay;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setCategories (ArrayList<String> newCategories)
    {
        final String constant = "Categories";
        try
        {
            String writeBuffer = "";
            //read the desktop file
            FileReader reader = new FileReader(desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);


            boolean written = false;
            String line = null;
            boolean firstLine = true;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                if (!firstLine)
                    writeBuffer = writeBuffer + "\n";
                if (line.startsWith(constant) && !written)
                {
                    written = true;
                    writeBuffer = writeBuffer + constant + "=";
                    for (String s : newCategories)
                    {
                        System.out.println(s);
                        writeBuffer = writeBuffer + s + ";";
                    }
                }
                else
                {
                    writeBuffer = writeBuffer + line;
                }
                firstLine = false;
            }
            //close the readers
            bufferedReader.close();
            reader.close();

            //init file writer
            FileWriter writer = new FileWriter(desktopFile);
            //write the buffer
            if (!written)
            {
                writer.write("[Desktop Entry]\n" + constant + "=");
                for (String s : categories)
                {
                    writer.write(s + ";");
                }
                writer.append(writeBuffer.substring(writeBuffer.indexOf("[Desktop Entry]") + 15));
            }
            else
                writer.write(writeBuffer);
            writer.close();

            categories = newCategories;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setIcon (String newIcon)
    {
        final String constant = "Icon";
        try
        {
            String writeBuffer = "";
            //read the desktop file
            FileReader reader = new FileReader(desktopFile);
            BufferedReader bufferedReader = new BufferedReader(reader);


            boolean written = false;
            String line = null;
            boolean firstLine = true;

            //go over each line and see if it matters
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line);
                if (!firstLine)
                    writeBuffer = writeBuffer + "\n";
                if (line.startsWith(constant) && !written)
                {
                    written = true;
                    writeBuffer = writeBuffer + constant + "=" + newIcon;
                }
                else
                {
                    writeBuffer = writeBuffer + line;
                }
                firstLine = false;
            }
            //close the readers
            bufferedReader.close();
            reader.close();
            //init file writer
            FileWriter writer = new FileWriter(desktopFile);
            //write the buffer
            if (!written)
            {
                writer.write("[Desktop Entry]\n" + constant + "=" + newIcon);
                writer.append(writeBuffer.substring(writeBuffer.indexOf("[Desktop Entry]") + 15));
            }
            else
                writer.write(writeBuffer);
            writer.close();

            icon = newIcon;
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

    public int getNumCategories()
    {
        return categories.size();
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

    public String printDebug()
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

    @Override
    public String toString()
    {
        return getName() + " (" + getDesktopFile().getName() + ")";
    }
}
