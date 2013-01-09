/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.interactions;

import java.util.ArrayList;
import java.util.Collection;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.constraints.filters.FilterConstraintsByInteraction;

/**
 * A list of interactions.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Interactions extends ArrayList<Interaction> {

	private ProteinHypernetwork hypernetwork;

	/**
	 * Constructor of class Interactions.
	 * 
	 * @param hypernetwork
	 *            the protein hypernetwork
	 */
	public Interactions(ProteinHypernetwork hypernetwork) {
		this.hypernetwork = hypernetwork;
	}

	/**
	 * Checks whether an interaction with a certain id exists.
	 * 
	 * @param id
	 *            the id
	 * @return true if the interaction exists
	 */
	public boolean containsInteractionWithId(String id) {
		return getInteractionWithId(id) != null;
	}

	/**
	 * Returns the interaction with a given id.
	 * 
	 * @param id
	 *            the id
	 * @return the interaction or null if none found
	 */
	public Interaction getInteractionWithId(String id) {
		for (Interaction i : this) {
			if (i.getId().equals(id)) {
				return i;
			} else if (i.getIdWithoutDomains().equals(id)) {
				return i;
			}
		}
		return null;
	}

	public Collection<Interaction> getInteractionsWithId(String id) {
		Collection<Interaction> interactions = new ArrayList<Interaction>();
		for (Interaction i : this) {
			if (i.getId().equals(id)) {
				interactions.add(i);
			} else if (i.getIdWithoutDomains().equals(id)) {
				interactions.add(i);
			}
		}
		return interactions;
	}

	@Override
	@SuppressWarnings("element-type-mismatch")
	public boolean remove(Object o) {
		if (o instanceof Interaction) {
			removeConstraints((Interaction) o);
		}
		return super.remove(o);
	}

	@Override
	public boolean removeAll(Collection<? extends Object> c) {
		for (Object o : c) {
			if (o instanceof Interaction)
				removeConstraints((Interaction) o);
		}
		return super.removeAll(c);
	}

	/**
	 * Remove all constraints with a given interaction.
	 * 
	 * @param i
	 *            the interaction
	 */
	private void removeConstraints(Interaction i) {
		Collection<Constraint> is = new FilterConstraintsByInteraction()
				.filter(hypernetwork.getConstraints(), i);
		hypernetwork.getConstraints().removeAll(is);
	}
}
