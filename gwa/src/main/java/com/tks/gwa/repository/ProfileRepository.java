package com.tks.gwa.repository;

import com.tks.gwa.entity.Profile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends GenericRepository<Profile, Integer> {

    public Profile findProfileByEmail(String email);

    public Profile createNewProfile(Profile profile);
}
