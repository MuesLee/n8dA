package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;


public class TypeEffectTest {
	
	private TypeEffect typeEffect;
	
	private StatusEffect otherStatusEffect;
	
	@Before
	public void setUp()
	{
		this.typeEffect = new TypeEffect();
		this.typeEffect.setId(80);
		this.typeEffect.setRoundsLeft(1);
		
		this.otherStatusEffect = new TypeEffect();
		this.otherStatusEffect.setId(81);
		this.otherStatusEffect.setRoundsLeft(1);
	}
	
	@Test
	public void testResolveVUL_Sci_And_VUL_Sci() throws Exception {
		
		typeEffect.setActionType(RobotActionType.SCISSOR);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.SCISSOR);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.VULNERABILITY);
		
		StatusEffect resolveInteractionWith = typeEffect.resolveInteractionWith(otherStatusEffect);
		
		assertNull(resolveInteractionWith);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(2, roundsLeft);
	}
	@Test
	public void testResolveVUL_Sci_And_RES_Sci() throws Exception {
		
		typeEffect.setActionType(RobotActionType.SCISSOR);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.SCISSOR);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		
		StatusEffect resolveInteractionWith = typeEffect.resolveInteractionWith(otherStatusEffect);
		
		assertNull(resolveInteractionWith);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(0, roundsLeft);
	}
	@Test
	public void testResolveVUL_Sci_And_RES_Sci_newEffect() throws Exception {
		
		typeEffect.setActionType(RobotActionType.SCISSOR);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		otherStatusEffect.setRoundsLeft(2);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.SCISSOR);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		
		StatusEffect resolveInteractionWith = typeEffect.resolveInteractionWith(otherStatusEffect);
		
		TypeEffect expectedEffect = new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.RESISTANCE, 1);
		assertEquals(expectedEffect , resolveInteractionWith);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(-1, roundsLeft);
	}

}
