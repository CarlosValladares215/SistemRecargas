package com.mycompany.recargasproyectoex.percistencia;

import com.mycompany.recargasproyectoex.clases.Celular;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.recargasproyectoex.clases.Cliente;
import com.mycompany.recargasproyectoex.clases.Recarga;
import com.mycompany.recargasproyectoex.percistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 *
 * @author vcarl
 */
public class CelularJpaController implements Serializable {

    public CelularJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_RecargasProyectoEX_jar_1.0-SNAPSHOTPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Celular celular) {
        if (celular.getRecargas() == null) {
            celular.setRecargas(new ArrayList<Recarga>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = celular.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getIdClie());
                celular.setCliente(cliente);
            }
            List<Recarga> attachedRecargas = new ArrayList<Recarga>();
            for (Recarga recargasRecargaToAttach : celular.getRecargas()) {
                recargasRecargaToAttach = em.getReference(recargasRecargaToAttach.getClass(), recargasRecargaToAttach.getIdReca());
                attachedRecargas.add(recargasRecargaToAttach);
            }
            celular.setRecargas(attachedRecargas);
            em.persist(celular);
            if (cliente != null) {
                cliente.getCelulares().add(celular);
                cliente = em.merge(cliente);
            }
            for (Recarga recargasRecarga : celular.getRecargas()) {
                Celular oldCelularOfRecargasRecarga = recargasRecarga.getCelular();
                recargasRecarga.setCelular(celular);
                recargasRecarga = em.merge(recargasRecarga);
                if (oldCelularOfRecargasRecarga != null) {
                    oldCelularOfRecargasRecarga.getRecargas().remove(recargasRecarga);
                    oldCelularOfRecargasRecarga = em.merge(oldCelularOfRecargasRecarga);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Celular celular) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Celular persistentCelular = em.find(Celular.class, celular.getIdCel());
            Cliente clienteOld = persistentCelular.getCliente();
            Cliente clienteNew = celular.getCliente();
            List<Recarga> recargasOld = persistentCelular.getRecargas();
            List<Recarga> recargasNew = celular.getRecargas();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getIdClie());
                celular.setCliente(clienteNew);
            }
            List<Recarga> attachedRecargasNew = new ArrayList<Recarga>();
            for (Recarga recargasNewRecargaToAttach : recargasNew) {
                recargasNewRecargaToAttach = em.getReference(recargasNewRecargaToAttach.getClass(), recargasNewRecargaToAttach.getIdReca());
                attachedRecargasNew.add(recargasNewRecargaToAttach);
            }
            recargasNew = attachedRecargasNew;
            celular.setRecargas(recargasNew);
            celular = em.merge(celular);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getCelulares().remove(celular);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getCelulares().add(celular);
                clienteNew = em.merge(clienteNew);
            }
            for (Recarga recargasOldRecarga : recargasOld) {
                if (!recargasNew.contains(recargasOldRecarga)) {
                    recargasOldRecarga.setCelular(null);
                    recargasOldRecarga = em.merge(recargasOldRecarga);
                }
            }
            for (Recarga recargasNewRecarga : recargasNew) {
                if (!recargasOld.contains(recargasNewRecarga)) {
                    Celular oldCelularOfRecargasNewRecarga = recargasNewRecarga.getCelular();
                    recargasNewRecarga.setCelular(celular);
                    recargasNewRecarga = em.merge(recargasNewRecarga);
                    if (oldCelularOfRecargasNewRecarga != null && !oldCelularOfRecargasNewRecarga.equals(celular)) {
                        oldCelularOfRecargasNewRecarga.getRecargas().remove(recargasNewRecarga);
                        oldCelularOfRecargasNewRecarga = em.merge(oldCelularOfRecargasNewRecarga);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = celular.getIdCel();
                if (findCelular(id) == null) {
                    throw new NonexistentEntityException("The celular with id " + id + " no longer exists.");
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
            Celular celular;
            try {
                celular = em.getReference(Celular.class, id);
                celular.getIdCel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The celular with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = celular.getCliente();
            if (cliente != null) {
                cliente.getCelulares().remove(celular);
                cliente = em.merge(cliente);
            }
            List<Recarga> recargas = celular.getRecargas();
            for (Recarga recargasRecarga : recargas) {
                recargasRecarga.setCelular(null);
                recargasRecarga = em.merge(recargasRecarga);
            }
            em.remove(celular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Celular> findCelularEntities() {
        return findCelularEntities(true, -1, -1);
    }

    public List<Celular> findCelularEntities(int maxResults, int firstResult) {
        return findCelularEntities(false, maxResults, firstResult);
    }

    private List<Celular> findCelularEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Celular.class));
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

    public Celular findCelular(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Celular.class, id);
        } finally {
            em.close();
        }
    }

    public int getCelularCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Celular> rt = cq.from(Celular.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Celular findByNumero(String numero) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Celular c WHERE c.numero = :numero", Celular.class)
                    .setParameter("numero", numero)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // No encontrado
        } finally {
            em.close();
        }
    }

}
