package net.codjo.gui.toolkit.text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.text.JTextComponent;

public class JTextFieldAutoCompleter extends AutomaticCompletion {
    protected Collection<String> completeList = new ArrayList<String>();


    public JTextFieldAutoCompleter(JTextComponent comp, Collection<String> allList) {
        super(comp);
        this.completeList = allList;
    }


    @Override
    protected boolean updateRestrictedList() {
        String value = textComp.getText();
        if (value == null) {
            list.setListData(new String[0]);
            return true;
        }

        List<String> listSelected = new ArrayList<String>();
        for (String item : completeList) {
            if (item.toUpperCase().contains(value.toUpperCase())) {
                listSelected.add(item);
            }
        }
        Collections.sort(listSelected);
        list.setListData(listSelected.toArray());

        return true;
    }


    @Override
    protected void acceptedListItem(Object selected) {
        if (selected == null) {
            return;
        }

        textComp.setText(selected.toString());
    }


    public void updateAutoCompletedList(Collection<String> newList) {
        this.completeList.clear();
        this.completeList.addAll(newList);
    }
}
