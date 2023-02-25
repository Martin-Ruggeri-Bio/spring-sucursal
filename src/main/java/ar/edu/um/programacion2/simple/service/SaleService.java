package ar.edu.um.programacion2.simple.service;

import ar.edu.um.programacion2.simple.model.Detail;
import ar.edu.um.programacion2.simple.model.Sale;
import ar.edu.um.programacion2.simple.model.ShoppingCart;
import ar.edu.um.programacion2.simple.repository.SaleRepository;
import ar.edu.um.programacion2.simple.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DetailService detailService;

    public List<Sale> getSalesByClient(String userName){
        return this.saleRepository.findByClient_UserName(userName);
    }

    // generar ventas de acuerdo a los productos que el cliente tenga en su carrito
    public void createSale(String userName){
        // obtengo el usuario
        User client = this.userService.getByUserName(userName).get();
        // obtengo la lista de productos del carrito del cliente
        List<ShoppingCart> shoppingCartList = this.shoppingCartService.getListByClient(client.getUserName());
        // establecemos el formato decimal, para no tener tantos decimales
        DecimalFormat decimalFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        // calculamos el total
        // .stream(): devuelve un stream (flujo o secuencia) de elementos que pueden ser procesados uno a uno de forma eficiente de la lista shoppingCartList.
        // .mapToDouble(shoppingCartItem -> shoppingCartItem.getMenu().getPrecio()): este método se aplica a cada elemento del stream y devuelve un stream que contiene el precio de cada artículo.
        //      Para hacer esto, el método mapToDouble utiliza una función lambda que toma cada elemento de la lista (un objeto shoppingCartItem) y devuelve su precio como un valor double.
        // .sum(): finalmente, se llama al método sum en el stream resultante para calcular la suma total de los precios de todos los artículos en el carrito.
        //      El resultado se almacena en la variable total
        double total = shoppingCartList.stream().mapToDouble(shoppingCartItem -> shoppingCartItem.getMenu().getPrecio()
                * shoppingCartItem.getAmount()).sum();
        // genero la venta con el formato que obtuvimos
        Sale sale = new Sale(Double.parseDouble(decimalFormat.format(total)), new Date(), client);
        // guardo en la base de datos
        Sale saveSale = this.saleRepository.save(sale);
        // creo un detalle cor cada item del carrito
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Detail detail = new Detail();
            detail.setMenu(shoppingCart.getMenu());
            detail.setAmount(shoppingCart.getAmount());
            detail.setSale(saveSale);
            this.detailService.createDetail(detail);
        }
        // por ultimo limpio el carrito de compra
        this.shoppingCartService.cleanShoppingCart(client.getToken());
    }
}
