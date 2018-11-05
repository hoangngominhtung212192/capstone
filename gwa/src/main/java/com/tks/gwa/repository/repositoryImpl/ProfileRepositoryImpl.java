package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Profile;
import com.tks.gwa.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProfileRepositoryImpl extends GenericRepositoryImpl<Profile, Integer> implements ProfileRepository {

    @Override
    public Profile findProfileByEmail(String email) {
        String sql = "SELECT p FROM " + Profile.class.getName()+ " AS p WHERE p.email =:email";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("email", email);

        Profile profile = null;

        try {
            profile = (Profile) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return profile;
    }

    @Override
    public Profile createNewProfile(Profile profile) {

        Profile newProfile = this.create(profile);

        return newProfile;
    }

    @Override
    public Profile findProfileByAccountID(int accountID) {
        String sql = "SELECT p FROM " + Profile.class.getName()+ " AS p WHERE p.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        Profile profile = null;

        try {
            profile = (Profile) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return profile;
    }

    @Override
    public Profile updateProfile(Profile profile) {

        Profile updatedProfile = this.update(profile);

        return updatedProfile;
    }

    @Override
    public List<Profile> getTopRanking() {

        String sql = "SELECT p FROM " + Profile.class.getName() + " AS p WHERE p.account.status='Active' " +
                "ORDER BY (p.numberOfStars/p.numberOfRaters) DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setMaxResults(10);

        List<Profile> profileList = query.getResultList();

        return profileList;
    }
}
