package utility.bulletin;

public enum Switch implements AbstractBulletin {
	Enabled {
		public void push (String bullet, String[] posDesc, Object[] values) {
			pushBullet0(AdvancedGlobalBulletin.createBullet(bullet, posDesc, values));
		}
		
		public void push (String bullet, String[] posDesc) {
			pushBullet0(AdvancedGlobalBulletin.createBullet(bullet, posDesc));
		}
		
		private void pushBullet0 (String bullet) {
			assureStackSize();
			System.out.println(bullet);
			AdvancedGlobalBulletin.bullets.add(bullet);
		}
		
		private void assureStackSize () {
			if (AdvancedGlobalBulletin.MAXSIZE == AdvancedGlobalBulletin.bullets.size()) {
				int h= 0;
				for (int i = (int) (AdvancedGlobalBulletin.MAXSIZE * (1 - AdvancedGlobalBulletin.LOADFACTOR)); i > 0; --i) {
					AdvancedGlobalBulletin.bullets.remove();
					h++;
				}
				AdvancedGlobalBulletin.getSharedInstance().BULLETIN.Info.push("Removed " + h + " objects from Bulletin",new String[]{"GlobalBulletin","assureStackSize"});
			}
		}
	},
	
	Disabled {
	@Override
	public void push(String bullet, String[] posDesc, Object[] values) {
	}

	@Override
	public void push(String bullet, String[] posDesc) {			
	}
	};
}