package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;

public class ArtifactTypeDaoJpaImplTest extends AbstractJpaDaoTest implements
		ArtifactTypeDao {

	private ArtifactTypeDao artifactTypeDao;
	
	@Override
	public ArtifactType create(ArtifactType artifactType)
			throws InvalidEntityException, AlreadyExistsEntityException {
		artifactType = artifactTypeDao.create(artifactType);
		assertNotNull(artifactType.getId());
		return artifactType;
	}

	@Override
	public List<ArtifactType> findAll() {
		return artifactTypeDao.findAll();
	}

	@Override
	public ArtifactType load(Long arg0) throws EntityNotFoundException {
		ArtifactType artifactType = 
				artifactTypeDao.load(artifactTypeDao.findAll().get(0).getId());
		assertNotNull(artifactType.getId());
		return artifactType;
	}

	@Override
	public ArtifactType update(ArtifactType arg0) throws InvalidEntityException {
		ArtifactType artifactType = artifactTypeDao.findAll().get(0);
		artifactType.setDescription("Description2");	
		
		artifactType = artifactTypeDao.update(artifactType);
		assertEquals(artifactType.getDescription(), "Description2");
		
		return artifactType;
	}
	
	@Override
	public void remove(ArtifactType artifactType) {
		artifactTypeDao.remove(artifactType);
	}
	
	/**
     * @param artifactTypeDao the artifactTypeDao to set
    */
    public void setArtifactTypeDao(ArtifactTypeDao artifactTypeDao) {
        this.artifactTypeDao = artifactTypeDao;
    }

}
