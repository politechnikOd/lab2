package Model;

import javax.persistence.*;

@Entity
@Table(name = "level", schema = "maksruslan", catalog = "")
public class LevelEntity {
    private int levelid;
    private String levelname;
    private byte levelrate;

    @Id
    @Column(name = "levelid", nullable = false)
    public int getLevelid() {
        return levelid;
    }

    public void setLevelid(int levelid) {
        this.levelid = levelid;
    }

    @Basic
    @Column(name = "levelname", nullable = false, length = 255)
    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    @Basic
    @Column(name = "levelrate", nullable = false)
    public byte getLevelrate() {
        return levelrate;
    }

    public void setLevelrate(byte levelrate) {
        this.levelrate = levelrate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LevelEntity that = (LevelEntity) o;

        if (levelid != that.levelid) return false;
        if (levelrate != that.levelrate) return false;
        if (levelname != null ? !levelname.equals(that.levelname) : that.levelname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = levelid;
        result = 31 * result + (levelname != null ? levelname.hashCode() : 0);
        result = 31 * result + (int) levelrate;
        return result;
    }
}
