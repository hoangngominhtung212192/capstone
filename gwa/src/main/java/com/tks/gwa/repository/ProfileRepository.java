package com.tks.gwa.repository;

import com.tks.gwa.entity.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends GenericRepository<Profile, Integer> {

    public Profile findProfileByEmail(String email);

    public Profile createNewProfile(Profile profile);

    public Profile findProfileByAccountID(int accountID);

    public Profile updateProfile(Profile profile);

    public List<Profile> getTopRanking();
}
