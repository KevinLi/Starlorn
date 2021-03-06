package edu.stuy.starlorn.entities;

import edu.stuy.starlorn.graphics.Anchor;

public class Bullet extends Entity {

    protected double speed, agility;
    protected boolean firedByPlayer, isSeeking;
    protected String[] sprites;
    protected int spriteIndex;
    protected Ship target;

    public Bullet(String[] sprites, double angle, double speed) {
        super(sprites[0]);
        this.angle = angle;
        this.speed = speed;
        this.sprites = sprites;
        firedByPlayer = isSeeking = false;
        spriteIndex = 0;
        setXvel(speed * Math.cos(angle));
        setYvel(speed * -Math.sin(angle));
    }

    public Bullet clone() {
        Bullet b = new Bullet(sprites, angle, speed);
        b.firedByPlayer = this.firedByPlayer;
        b.isSeeking = isSeeking;
        b.getRect().x = this.getRect().x;
        b.getRect().y = this.getRect().y;
        return b;
    }

    @Override
    public void step() {
        xvel = speed * Math.cos(angle);
        yvel = -speed * Math.sin(angle);
        if (isSeeking)
            seekTarget();
        super.step();
        if (!onScreen())
            kill();
        for (Ship that : world.getShips()) {
            if (that.isHit(this))
                explode(that);
        }
        if (sprites.length > 1) {
            spriteIndex++;
            if (spriteIndex >= sprites.length)
                spriteIndex = 0;
            updateSprite(sprites[spriteIndex], Anchor.TOP_CENTER);
        }
    }

    private void seekTarget() {
        if (target == null || target.isDead()) {
            isSeeking = false;
            return;
        }

        double targetx = target.getRect().x + target.getRect().width / 2,
               targety = target.getRect().y + target.getRect().height / 2,
               xdiff = targetx - (rect.x + rect.width / 2),
               ydiff = targety - (rect.y + rect.height / 2),
               targetAngle = -Math.atan2(ydiff, xdiff),
               delta = Math.atan2(Math.sin(targetAngle - angle),
                                  Math.cos(targetAngle - angle));

        if (Math.abs(delta) <= agility)
            angle += delta;
        else if (delta > 0)
            angle += agility;
        else
            angle -= agility;

        setXvel(speed * Math.cos(angle));
        setYvel(speed * -Math.sin(angle));
    }

    private void explode(Ship that) {
        this.kill();
        that.kill();
        Explosion e = new Explosion();
        double thatcx = that.getRect().x + that.getRect().width / 2,
               thatcy = that.getRect().y + that.getRect().height / 2;
        e.getRect().x = thatcx - e.getRect().width / 2;
        e.getRect().y = thatcy - e.getRect().height / 2;
        world.addEntity(e);
    }

    public void seek(double agility, Ship target) {
        isSeeking = true;
        this.agility = agility;
        this.target = target;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSeeking(boolean seek) {
        this.isSeeking = seek;
    }

    public boolean getSeeking() {
        return isSeeking;
    }

    public void setFiredByPlayer(boolean value) {
        firedByPlayer = value;
    }

    public boolean wasFiredByPlayer() {
        return firedByPlayer;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double ang) {
        angle = ang;
    }

    public void setSprites(String[] spr) {
        sprites = spr;
        updateSprite(sprites[0]);
    }

    public String[] getSprites() {
        return sprites;
    }
}
