package mateus.andrade.milhas.Models;

import java.io.Serializable;

/**
 * Created by mateusandrade on 03/01/2018.
 */

public class Fare implements Serializable{

    String totalfare;

    public String getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(String totalfare) {
        this.totalfare = totalfare;
    }
}
