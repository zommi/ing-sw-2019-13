package server.controller.playeraction;

import server.controller.playeraction.ShootInfo;

public interface WeaponRulesInterface {

    public boolean validate(ShootInfo pack);

    public void actuate(ShootInfo pack);

    public void unpack(ShootInfo pack);
}
