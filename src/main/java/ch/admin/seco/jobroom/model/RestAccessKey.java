package ch.admin.seco.jobroom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AOSTE_ACCESSKEYS")
public class RestAccessKey {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCESSKEY")
    private String accessKey;

    @Column(name = "OWNER")
    private String keyOwner;

    @Column(name = "ACTIVE_B")
    private int active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getKeyOwner() {
        return keyOwner;
    }

    public void setKeyOwner(String keyOwner) {
        this.keyOwner = keyOwner;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RestAccessKey{" +
                "id=" + id +
                ", accessKey='" + accessKey + '\'' +
                ", keyOwner='" + keyOwner + '\'' +
                ", active=" + active +
                '}';
    }
}
