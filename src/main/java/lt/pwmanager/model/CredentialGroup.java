package lt.pwmanager.model;

import java.util.ArrayList;
import java.util.List;

public class CredentialGroup {
    private String name;
    private List<Credentials> credentials;

    public CredentialGroup() {
        this.credentials = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Credentials> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credentials> credentials) {
        this.credentials = credentials;
    }

    public void addCredentials(Credentials credentials) {
        this.credentials.add(credentials);
    }

    @Override
    public String toString() {
        return name;
    }
}
