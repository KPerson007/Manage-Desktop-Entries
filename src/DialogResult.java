/**
 * Created by root on 11/22/16.
 */
public class DialogResult {
    private boolean success;
    private String input;

    public DialogResult(boolean success, String input)
    {
        this.success = success;
        this.input = input;
    }

    public DialogResult(boolean success) //only use this constructor is success = false
    {
        this.success = success;
        input = null;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public String getInput()
    {
        return input;
    }

    @Override
    public String toString()
    {
        return getInput();
    }
}
