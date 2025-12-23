package com.example.quiz_1141013.vo;

public class OptionsCount  extends Options{

		private int count;

		public OptionsCount() {
			super();
			// TODO Auto-generated constructor stub
		}

		public OptionsCount(int code, String optionName, int count) {
			super(code, optionName);
			this.count = count;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
		
}

