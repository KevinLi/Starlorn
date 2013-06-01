package edu.stuy.starlorn.entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import edu.stuy.starlorn.graphics.Sprite;
import edu.stuy.starlorn.upgrades.GunUpgrade;

public class Ship extends Entity {

    protected LinkedList<GunUpgrade> gunUpgrades;
    protected int baseDamage, baseShotSpeed, health, maxHealth, cooldownTimer,
                  baseCooldown, cooldownRate, maxSpeed;
    protected double baseAim;
    protected boolean shootRequested;

    public Ship(double x, double y, String name) {
        super(x, y, name);
        gunUpgrades = new LinkedList<GunUpgrade>();
        gunUpgrades.add(new GunUpgrade()); // add default gunupgrade
        baseDamage = 1;
        baseShotSpeed = 12;
        maxHealth = 10;
        health = maxHealth;
        baseAim = Math.PI / 2; //Aim up by default
        baseCooldown = 10;
        cooldownTimer = 0;
        cooldownRate = 1;
        maxSpeed = 10;
        shootRequested = false;
    }

    public Ship(String name) {
        this(0, 0, name);
    }

    public Ship(double x, double y) {
        this(x, y, null);
    }

    public Ship() {
        this(null);
    }

    public Ship clone() {
        Ship s = new Ship();
        s.sprite = sprite;
        s.baseDamage = baseDamage;
        s.baseShotSpeed = baseShotSpeed;
        s.maxHealth = maxHealth;
        s.health = maxHealth;
        s.baseCooldown = baseCooldown;
        s.cooldownRate = cooldownRate;
        s.maxSpeed = maxSpeed;
        s.baseAim = baseAim;
        return s;
    }

    public void addUpgrade(GunUpgrade upgrade) {
        gunUpgrades.add(upgrade);
    }

    public boolean isHit(Bullet b) {
        Rectangle2D.Double brect = b.getRect();
        return (brect.x + brect.width  > rect.x && brect.x < rect.x + rect.width &&
                brect.y + brect.height > rect.y && brect.y < rect.y + rect.height);
    }

    /*
     * Create the shots based on the available GunUpgrades
     */
    public void shoot() {
        GunUpgrade topShot = gunUpgrades.get(0);
        int damage = baseDamage;
        int shotSpeed = baseShotSpeed;
        int cooldown = baseCooldown;
        for (GunUpgrade up : gunUpgrades) {
            if (up.getNumShots() > topShot.getNumShots())
                topShot = up;
            damage = up.getDamage(damage);
            shotSpeed = up.getShotSpeed(shotSpeed);
            cooldown = up.getCooldown(cooldown);
        }
        // Create new shots, based on dem vars
        int numShots = topShot.getNumShots();
        for (int i = 0; i < numShots; i++) {
            Bullet b = new Bullet(baseAim + topShot.getAimAngle(), damage,
                    shotSpeed);
            double centerx = rect.x + rect.width / 2 - b.getRect().width / 2;
            b.getRect().x = centerx + topShot.getXOffset();
            b.getRect().y = rect.y + 10;
            b.setWorld(this.getWorld());
        }
        cooldownTimer = cooldown;
    }
    @Override
    public void step() {
        //Only baseCooldown if we're below the rate, otherwise the ship hasn't tried to shoot
        if (cooldownTimer <= 0 && shootRequested) {
            this.shoot();
        } else {
            cooldownTimer -= cooldownRate;
        }
        super.step();
    }

    public void setShootRequested(boolean shoot) {
        shootRequested = shoot;
    }

    public boolean getShootRequested() {
        return shootRequested;
    }

    public void setBaseDamage(int damage) {
        baseDamage = damage;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseShotSpeed(int speed) {
        baseShotSpeed = speed;
    }

    public int getBaseShotSpeed() {
        return baseShotSpeed;
    }

    public void setMaxHealth(int health) {
        maxHealth = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setBaseCooldown(int baseCooldown) {
        this.baseCooldown = baseCooldown;
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    public void setCooldownRate(int rate) {
        cooldownRate = rate;
    }

    public int getCooldownRate() {
        return cooldownRate;
    }

    public void setMovementSpeed(int speed) {
        maxSpeed = speed;
    }

    public int getMovementSpeed() {
        return maxSpeed;
    }
}
