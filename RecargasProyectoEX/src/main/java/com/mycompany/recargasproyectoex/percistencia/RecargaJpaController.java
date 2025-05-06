
package com.mycompany.recargasproyectoex.percistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.recargasproyectoex.clases.Celular;
import com.mycompany.recargasproyectoex.clases.Recarga;
import com.mycompany.recargasproyectoex.percistencia.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author vcarl
 */
public class RecargaJpaController implements Serializable {

    public RecargaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_RecargasProyectoEX_jar_1.0-SNAPSHOTPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recarga recarga) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celular celular = recarga.getCelular();
            if (celular != null) {
                celular = em.getReference(celular.getClass(), celular.getIdCel());
                recarga.setCelular(celular);
            }
            em.persist(recarga);
            if (celular != null) {
                celular.getRecargas().add(recarga);
                celular = em.merge(celular);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recarga recarga) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recarga persistentRecarga = em.find(Recarga.class, recarga.getIdReca());
            Celular celularOld = persistentRecarga.getCelular();
            Celular celularNew = recarga.getCelular();
            if (celularNew != null) {
                celularNew = em.getReference(celularNew.getClass(), celularNew.getIdCel());
                recarga.setCelular(celularNew);
            }
            recarga = em.merge(recarga);
            if (celularOld != null && !celularOld.equals(celularNew)) {
                celularOld.getRecargas().remove(recarga);
                celularOld = em.merge(celularOld);
            }
            if (celularNew != null && !celularNew.equals(celularOld)) {
                celularNew.getRecargas().add(recarga);
                celularNew = em.merge(celularNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = recarga.getIdReca();
                if (findRecarga(id) == null) {
                    throw new NonexistentEntityException("The recarga with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recarga recarga;
            try {
                recarga = em.getReference(Recarga.class, id);
                recarga.getIdReca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recarga with id " + id + " no longer exists.", enfe);
            }
            Celular celular = recarga.getCelular();
            if (celular != null) {
                celular.getRecargas().remove(recarga);
                celular = em.merge(celular);
            }
            em.remove(recarga);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recarga> findRecargaEntities() {
        return findRecargaEntities(true, -1, -1);
    }

    public List<Recarga> findRecargaEntities(int maxResults, int firstResult) {
        return findRecargaEntities(false, maxResults, firstResult);
    }

    private List<Recarga> findRecargaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recarga.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Recarga findRecarga(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recarga.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recarga> rt = cq.from(Recarga.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
