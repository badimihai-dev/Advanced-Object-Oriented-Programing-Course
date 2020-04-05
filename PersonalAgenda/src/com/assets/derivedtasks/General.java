package com.assets.derivedtasks;

import com.assets.Task;

public class General extends Task {
    private int importancy;

    public General() {
        super();
        this.importancy = 0;
    };

    public General(String title, boolean status, int importancy){
        super(title, status);
        this.importancy =  importancy;
    }

    public void updateImportancy(int imp){
        if(imp < 0 || imp > 3){
            System.out.println("Invalid importancy value.");
            return;
        }
        this.importancy = imp;
    }

    public String getImportancyString(){
        String imp;
        switch (importancy){
            case 1:
                imp = "Medium";
                break;
            case 2:
                imp = "High";
                break;
            default:
                imp = "Low";
                break;
        }
        return imp;
    }

    public int getImportancy() {
        return importancy;
    }

    @Override
    public void Print(){
        super.Print();
        System.out.println("|-Importancy: " + getImportancyString());
    }
}
