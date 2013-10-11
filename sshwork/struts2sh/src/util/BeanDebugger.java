package util;
import java.beans.PropertyDescriptor;
import java.util.Vector;

/**
 * Debugger at runtime, internal used by BeanSoft.
 */
public class BeanDebugger {
    /**
     * ����, ��ӡ������ Bean ���������Ե�ȡֵ.
     * @date 2005-07-31
     * @author BeanSoft
     * @param bean ��Ҫ���ԵĶ���
     */
    public static void dump(Object bean) {
        java.beans.PropertyDescriptor[] descriptors =
            getAvailablePropertyDescriptors(bean);

        for(int i = 0; descriptors != null && i < descriptors.length; i++) {
            java.lang.reflect.Method readMethod = descriptors[i].getReadMethod();

            try {
                Object value = readMethod.invoke(bean, null);
                System.out.println("[" + bean.getClass().getName() + "]." +
                        descriptors[i].getName() + "(" + 
                        descriptors[i].getPropertyType().getName() + ") = "
                        + value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * �� bean �ж�ȡ��Ч������������.
	 *
	 * NOTE: ����Ϊ class �� PropertyDescriptor ���ų�����.
	 *
	 * @param bean
	 *            Object - ��Ҫ��ȡ�� Bean
	 * @return PropertyDescriptor[] - �����б�
	 */
	public static java.beans.PropertyDescriptor[] getAvailablePropertyDescriptors(
			Object bean) {
		try {
			// �� Bean �н���������Ϣ��������ص� write ����
			java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(bean
					.getClass());
			if (info != null) {
				java.beans.PropertyDescriptor pd[] = info
						.getPropertyDescriptors();
				Vector columns = new Vector();

				for (int i = 0; i < pd.length; i++) {
					String fieldName = pd[i].getName();

					if (fieldName != null && !fieldName.equals("class")) {
						columns.add(pd[i]);
					}
				}

				java.beans.PropertyDescriptor[] arrays = new java.beans.PropertyDescriptor[columns
						.size()];

				for (int j = 0; j < columns.size(); j++) {
					arrays[j] = (PropertyDescriptor) columns.get(j);
				}

				return arrays;
			}
		} catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
		return null;
	}

}