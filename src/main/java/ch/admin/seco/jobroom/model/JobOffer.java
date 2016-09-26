package ch.admin.seco.jobroom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
public class JobOffer {

    @Id
    @GeneratedValue(generator="joboffer_gen")
    @SequenceGenerator(name="joboffer_gen", sequenceName="aoste_seq")
    private Integer id;

    @Version
    @JsonIgnore
    private Integer version;

    @NotNull
    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private String reference;

    private String url;

    @Embedded
    @Valid
    @NotNull
    private Job job;

    @Embedded
    @NotNull
    private Company company;

    @Embedded
    @NotNull
    private Contact contact;

    @Embedded
    @NotNull
    private Application application;

    @ManyToOne
    @JoinColumn(name="owner_id")
    @JsonIgnore
    private RestAccessKey owner;

    @JsonIgnore
    private Timestamp creationDate;

    @JsonIgnore
    private Timestamp lastModificationDate;

    @JsonIgnore
    private Timestamp cancellationDate;

    @JsonIgnore
    private String cancellationReasonCode;

    @PrePersist
    void onCreate() {
        creationDate = new Timestamp(DateTime.now().getMillis());
    }

    @PreUpdate
    void onUpdate() {
        lastModificationDate = new Timestamp(DateTime.now().getMillis());
    }

    public JobOffer() {
    }

    public JobOffer(Integer id, Integer version,
                    LocalDate publicationStartDate, LocalDate publicationEndDate,
                    String reference, String url,
                    Job job, Company company, Contact contact, Application application,
                    RestAccessKey owner, Timestamp creationDate, Timestamp lastModificationDate,
                    Timestamp cancellationDate, String cancellationReasonCode) {
        this.version = version;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.reference = reference;
        this.url = url;
        this.job = job;
        this.company = company;
        this.contact = contact;
        this.application = application;
        this.owner = owner;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
        this.cancellationDate = cancellationDate;
        this.cancellationReasonCode = cancellationReasonCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public void setPublicationStartDate(LocalDate publicationStartDate) {
        this.publicationStartDate = publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public void setPublicationEndDate(LocalDate publicationEndDate) {
        this.publicationEndDate = publicationEndDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public RestAccessKey getOwner() {
        return owner;
    }

    public void setOwner(RestAccessKey owner) {
        this.owner = owner;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Timestamp lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Timestamp getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Timestamp cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReasonCode() {
        return cancellationReasonCode;
    }

    public void setCancellationReasonCode(String cancellationReasonCode) {
        this.cancellationReasonCode = cancellationReasonCode;
    }
}
