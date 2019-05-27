package view;

import server.model.cards.WeaponCard;

import java.util.List;

public class ListOfWeaponsAnswer implements ServerAnswer {
    List<WeaponCard> list;

    public void add(WeaponCard weap){
       list.add(weap);
    }

    public List<WeaponCard> getList(){
        return list;
    }
}
