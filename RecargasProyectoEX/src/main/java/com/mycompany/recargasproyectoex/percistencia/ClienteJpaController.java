package com.mycompany.recargasproyectoex.percistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.recargasproyectoex.clases.Celular;
import com.mycompany.recargasproyectoex.clases.Cliente;
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
public class ClienteJpaController implements Serializable {

    public ClienteJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_RecargasProyectoEX_jar_1.0-SNAPSHOTPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getCelulares() == null) {
            cliente.setCelulares(new ArrayList<Celular>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Celular> attachedCelulares = new ArrayList<Celular>();
            for (Celular celularesCelularToAttach : cliente.getCelulares()) {
                celularesCelularToAttach = em.getReference(celularesCelularToAttach.getClass(), celularesCelularToAttach.getIdCel());
                attachedCelulares.add(celularesCelularToAttach);
            }
            cliente.setCelulares(attachedCelulares);
            em.persist(cliente);
            for (Celular celularesCelular : cliente.getCelulares()) {
                Cliente oldClienteOfCelularesCelular = celularesCelular.getCliente();
                celularesCelular.setCliente(cliente);
                celularesCelular = em.merge(celularesCelular);
                if (oldClienteOfCelularesCelular != null) {
                    oldClienteOfCelularesCelular.getCelulares().remove(celularesCelular);
                    oldClienteOfCelularesCelular = em.merge(oldClienteOfCelularesCelular);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdClie());
            List<Celular> celularesOld = persistentCliente.getCelulares();
            List<Celular> celularesNew = cliente.getCelulares();
            List<Celular> attachedCelularesNew = new ArrayList<Celular>();
            for (Celular celularesNewCelularToAttach : celularesNew) {
                celularesNewCelularToAttach = em.getReference(celularesNewCelularToAttach.getClass(), celularesNewCelularToAttach.getIdCel());
                attachedCelularesNew.add(celularesNewCelularToAttach);
            }
            celularesNew = attachedCelularesNew;
            cliente.setCelulares(celularesNew);
            cliente = em.merge(cliente);
            for (Celular celularesOldCelular : celularesOld) {
                if (!celularesNew.contains(celularesOldCelular)) {
                    celularesOldCelular.setCliente(null);
                    celularesOldCelular = em.merge(celularesOldCelular);
                }
            }
            for (Celular celularesNewCelular : celularesNew) {
                if (!celularesOld.contains(celularesNewCelular)) {
                    Cliente oldClienteOfCelularesNewCelular = celularesNewCelular.getCliente();
                    celularesNewCelular.setCliente(cliente);
                    celularesNewCelular = em.merge(celularesNewCelular);
                    if (oldClienteOfCelularesNewCelular != null && !oldClienteOfCelularesNewCelular.equals(cliente)) {
                        oldClienteOfCelularesNewCelular.getCelulares().remove(celularesNewCelular);
                        oldClienteOfCelularesNewCelular = em.merge(oldClienteOfCelularesNewCelular);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = cliente.getIdClie();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdClie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<Celular> celulares = cliente.getCelulares();
            for (Celular celularesCelular : celulares) {
                celularesCelular.setCliente(null);
                celularesCelular = em.merge(celularesCelular);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Cliente findByCedula(String cedula) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.cedula = :cedula", Cliente.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

}
